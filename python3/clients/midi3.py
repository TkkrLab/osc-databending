#!/usr/bin/env python
 
import sys 
from pygame import *
import pygame.midi
from pythonosc.dispatcher import Dispatcher
from pythonosc.udp_client import SimpleUDPClient

# midi programming examples
# https://audiodestrukt.wordpress.com/2013/06/23/midi-programming-in-python/
# https://bitbucket.org/pygame/pygame/src/25e3f2cee879/examples/midi.py

def main():
    init()
    pygame.midi.init()

    argc = len(sys.argv)

    if argc < 4:
        print("usage: " + sys.argv[0] + " <IP address> <port> <midi dev_no> [OSC name]")
        # list all midi output devices
        for x in range( 0, pygame.midi.get_count() ):
            dev_info = pygame.midi.get_device_info(x)
            if dev_info[2] == 1:
                print(x,dev_info)
        
        return

    dispatcher = Dispatcher()
    client = SimpleUDPClient(sys.argv[1], int(sys.argv[2]))
    name = sys.argv[4] if argc > 4 else "pymidi"
    # aankondigen output ports
    client.send_message("/" + name + "/out/outputs", "ch1,ch2,ch3,ch4,ch5,ch6,ch7,ch8")

    midi_input = pygame.midi.Input(int(sys.argv[3]))
    try:
        while True:
            if midi_input.poll():
                # no way to find number of messages in queue
                # so we just specify a high max value
                evlist = midi_input.read(1000)
                for x in range( 0, len(evlist)):
                   ev = evlist[x][0]
                   status,data1,data2,data3 = ev;  
                   print( str(status) + ", " + str(data1) + ", " + str(data2) + ", " + str(data3))
                   if (status == 176):
                        value = data2 / 128.0
                        client.send_message("/" + name + "/out/ch" + str(data1), value)

            time.wait(10)
    finally:
        del midi_input
        pygame.midi.quit() 
    quit()
 
 
if __name__ == '__main__':
    main()
