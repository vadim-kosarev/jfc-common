#!/usr/bin/python
# a simple tcp server
import socket
import threading
import time

# ==========================================================================
def processClient(connection, address):
    toRun = True
    pid = threading.current_thread().ident
    while toRun:
        buf = connection.recv(1024)
        if (len(buf) > 0):
            print("[{pid}] Recv from {addr}: {buf}".format(
                pid=str(pid), addr=str(address), buf=str(buf)))
            connection.send(buf)
        else:
            time.sleep(120)
            toRun = False

    connection.close()

# ==========================================================================
# MAIN ---------------------------------------------------------------------
sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)  
sock.bind(('0.0.0.0', 12345))  
sock.listen(5)  
while True:  
    connection,address = sock.accept()
    threading.Thread(target=processClient, args=[connection, address]).start()

# ==========================================================================
