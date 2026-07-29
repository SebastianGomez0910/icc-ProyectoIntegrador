import requests
import time

BASE_URL = "http://localhost:8080/api"

# Pega aquí un token válido que hayas obtenido desde Swagger al iniciar sesión
TOKEN_JWT = "eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJvcmdhbml6YWRvcjNAZWplbXBsby5jb20iLCJpYXQiOjE3ODUzMDI5NTAsImV4cCI6MTc4NTM4OTM1MH0.iscqx8V2MmsNOpfDij8fueQs54pDQZar_WPn___ky1EEXtxREep9i-mlgdwQkvH_"

headers = {
    "Content-Type": "application/json"
}

def probar_endpoint(nombre, endpoint, max_limite, payload=None, method="POST", token=None):
    url = f"{BASE_URL}{endpoint}"
    print(f"\n==========================================")
    print(f" Probando límite para: [{nombre}] -> {url}")
    print(f" Límite esperado: {max_limite} peticiones")
    print(f"==========================================")

    req_headers = headers.copy()
    if token:
        req_headers["Authorization"] = f"Bearer {token}"

    # Enviamos el límite exacto + 2 peticiones extra para forzar el bloqueo
    total_a_enviar = max_limite + 2

    for i in range(1, total_a_enviar + 1):
        try:
            if method == "GET":
                response = requests.get(url, headers=req_headers)
            else:
                response = requests.post(url, json=payload, headers=req_headers)
            
            print(f"Petición #{i:02d} -> HTTP {response.status_code}")
            
            # Validación del Rate Limit cumpliendo la rúbrica
            if response.status_code == 429:
                print(f"  [¡ÉXITO!] Bloqueo 429 alcanzado en la petición #{i}.")
                # Capturamos el Retry-After como exige el Punto 7
                retry_after = response.headers.get("Retry-After", "No encontrado")
                print(f"  Header 'Retry-After': {retry_after} segundos.")
                print(f"  Cuerpo: {response.text}")
                break
            elif response.status_code in [401, 403]:
                print(f"  [ALERTA] Error de permisos. Revisa que el TOKEN sea válido.")
                break
            elif response.status_code >= 500:
                print(f"  [ERROR] El servidor falló (Código {response.status_code}).")
            
        except requests.exceptions.ConnectionError:
            print(f"Petición #{i:02d} -> Error: No se pudo conectar al servidor.")
            break

        # Pequeña pausa para no saturar los hilos de red de tu propia PC
        time.sleep(0.05)

if __name__ == "__main__":
    print("Iniciando pruebas integrales de Rate Limit...\n")

    # 1. Prueba de Endpoints Públicos (Límite: 60)
    # Cambia la ruta por una que sea pública en tu API, ej. GET /events/public
    probar_endpoint("Público", "/events", max_limite=60, method="GET")

    # 2. Prueba de Reportes Autenticados (Límite: 5)
    # Usamos el ID 25 del evento que creaste en el paso anterior
    probar_endpoint("Reportes", "/reports/events/25/registrations.pdf", max_limite=5, method="GET", token=TOKEN_JWT)