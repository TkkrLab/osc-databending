# OSC Databending
For the databending event during GOGBOT 2011 Tkkrlab contributed several projects. Purpose of the Databending event was to connect many art / technology installations together.

## Protocol
The OSC protocol is just a way to build messages, the network protocol is used most often. The contents of the messages and / or the relation between senders and receivers is not described. Therefore Heinze Havinga invented this

 [communication structure](https://docs.google.com/document/d/1s-i1LKZHQY8iHEqjugJaqyl0D3vtgzAk7dsgkeoPnbk/edit?hl=en_US) (written in Dutch)

## Projects
### Databender
A [Java web application](java) and a [Python3 web application](python3/datadirigent) using [Twisted](https://twistedmatrix.com/trac/) are available 
### Oscilloscope
Put the oscilloscope in X/Y mode and connect the X channel to the left audio output and the Y channel to the right audio output.
#### CSound
[spiro-osc.csd](csound/spiro-osc.csd) shows Spirograph figures. It contains two "instruments", the first one uses only one wheel but you can vary the output by gat,fase,wiel,fase2, and figs

-   gat: hole in spirograph wheel
-   fase: rotate figure
-   wiel: spirograph wheel
-   fase2: rotation of subsequent figures
-   figs: number of figure to draw

The second instrument draws predefined spirograph figure where you can only vary 'gat' and 'fase'. qutecsound (available as a ubuntu package) is a nice IDE for playing with CSound.

### Music
#### Csound Drone
[Csound](http://www.csound.com/) is a language to describe waveforms (orchestras) and music scores. It supports OSC. Csound Drone based on a Csound program by Michael Gogins is controllable via OSC. The original program is part of his [Csound Algorithmic Composition Tutorial](http://michael-gogins.com/archives/Csound_Algorithmic_Composition_Tutorial.pdf). Two versions are available: [Csound Drone for Android Fingerplay](csound/drone-fingerplay.csd) and [Csound Drone for Databender](csound/drone.csd)

##### Csound Drone for Android Fingerplay
Csound Drone for Android Fingerplay is a OSC server which can be controlled the Android OSC client [Fingerplay](android/fingerplay). The Fingerplay parameters are:
```
/fingerplay/control/1
/fingerplay/control/2
/fingerplay/control/4
/fingerplay/control/5
```
which corresponds to the XY-pads of Fingerplay. They are coupled to the following Csound Drone parameters:
```
kdistortfactor
kfeedback
kharmonic
kdisttable
```
