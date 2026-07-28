import requests
import time

BASE_URL = "http://localhost:8080/api"

headers = {
    "Content-Type": "application/json"
}

def probar_endpoint(nombre, endpoint, max_limite, payload=None, method="POST", token=None):
    url = f"{BASE_URL}{endpoint}"
    print(f"\n==========================================")
    print(f" Probando límite para: [{nombre}] -> {url}")
    print(f" Límite configurado esperado: {max_limite} peticiones")
    print(f"==========================================")

    req_headers = headers.copy()
    if token:
        req_headers["Authorization"] = f"Bearer {token}"

    total_a_enviar = max_limite + 2

    for i in range(1, total_a_enviar + 1):
        try:
            if method == "GET":
                response = requests.get(url, headers=req_headers)
            else:
                response = requests.post(url, json=payload, headers=req_headers)
            
            print(f"Petición #{i:02d} -> HTTP {response.status_code}")
            
            if response.status_code == 429:
                print(f"  [¡BLOQUEADO CORRECTAMENTE!] Código 429 Too Many Requests alcanzado en la petición #{i}.")
                print(f"  Respuesta del servidor: {response.text}")
                break
            elif response.status_code >= 500:
                print(f"  [ERROR SERVIDOR] Código {response.status_code}.")
            
        except requests.exceptions.ConnectionError:
            print(f"Petición #{i:02d} -> Error: No se pudo conectar al servidor.")
            break

        time.sleep(0.05)

if __name__ == "__main__":
    print("Iniciando pruebas integrales de Rate Limit...\n")

    payload_registro = {
        "email": "test_rate@ups.edu.ec",
        "password": "Password123*",
        "name": "Usuario Test"
    }
    probar_endpoint("Registro", "/auth/register", max_limite=3, payload=payload_registro, method="POST")

    probar_endpoint("Público", "/public/test", max_limite=60, method="GET")

    TOKEN_JWT_OPCIONAL = None 
    probar_endpoint("Reportes", "/reports", max_limite=5, method="GET", token=TOKEN_JWT_OPCIONAL)

    probar_endpoint("General / Autenticado", "/events", max_limite=120, method="GET", token=TOKEN_JWT_OPCIONAL)

    print("\n¡Pruebas de Rate Limit finalizadas!")