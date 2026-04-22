import socket
import threading

SERVER_IP = "100.79.133.37"
PORT = 5000

client = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
client.connect((SERVER_IP, PORT))

def receive():
    while True:
        try:
            msg = client.recv(1024).decode("utf-8")
            if not msg:
                break
            print("\nServer:", msg)
        except:
            break

threading.Thread(target=receive, daemon=True).start()

while True:
    msg = input()
    client.sendall((msg + "\n").encode("utf-8"))