import sys
import time
import random
from pythonosc.dispatcher import Dispatcher
from pythonosc.osc_server import BlockingOSCUDPServer
from pythonosc.udp_client import SimpleUDPClient

def main():
    if len(sys.argv) < 3:
        print("usage: " + sys.argv[0] + " <IP address> <port> [OSC name]")
        return
    dispatcher = Dispatcher()
    client = SimpleUDPClient(sys.argv[1], int(sys.argv[2]))


    name = sys.argv[3] if len(sys.argv) > 3 else "channels"

    channels = 8
    channel = [0] * channels
    direction = [0.01] * channels
    cstr = ""
    for c in range(channels):
        if c > 0:
            cstr += ","
        cstr += "ch" + str(c+1)

    client.send_message("/" + name + "/out/outputs", cstr)

    while True:
        for c in range(channels):
            if channel[c] >= 1.0:
                channel[c] = 1.0
                direction[c] = -0.002 * random.randint(1, 3)
            if channel[c] <= 0.0:
                channel[c] = 0.0
                direction[c] = 0.002 * random.randint(1, 3)
            client.send_message("/" + name + "/out/ch" + str(c+1), channel[c])
            channel[c] += direction[c]
        time.sleep(0.02)

if __name__ == '__main__':
    main()
