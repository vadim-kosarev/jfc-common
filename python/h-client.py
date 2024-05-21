#!/usr/bin/python
import socket
import sys
import time
import threading


HOST = 'localhost'    # The remote host
PORT =  12345              # The same port as used by the server

# ===================================================================

def runClient(host, port):
    pid = threading.current_thread().ident
    with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
        print("Connecting to {}:{}".format(host, port))
        s.connect((host, port))
        i = 0
        loops = 30
        while i < loops:
            i += 1
            s.sendall(bytes(
                "Hello {i} World".format(pid=pid, i=i),
                            "utf-8"))
            data = s.recv(1024)
            print('[{pid}] Received {buf}'.format(pid = pid, buf = repr(data)))
            time.sleep(1)
        s.close()

# ===================================================================
# MAIN --------------------------------------------------------------
if len(sys.argv) >= 3:
    HOST = sys.argv[1]
    PORT = sys.argv[2]
for i in range(100):
    threading.Thread(target=runClient, args=[HOST, PORT]).start()
