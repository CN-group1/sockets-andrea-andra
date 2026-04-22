import socket
import threading

SERVER_IP = "100.79.133.37"
PORT = 5000

client = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
client.bind(("", 6000)) 

def receive():
    while True:
        try:
            data, addr = client.recvfrom(1024)
            print("\nServer:", data.decode("utf-8"))
        except Exception as e:
            print("Receive error:", e)
            break

threading.Thread(target=receive, daemon=True).start()

while True:
    msg = input()
    client.sendto(msg.encode("utf-8"), (SERVER_IP, PORT))