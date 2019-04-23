#!/usr/bin/env python
# -*- coding: utf-8 -*-

"""
PyFingerprint
Copyright (C) 2015 Bastian Raschke <bastian.raschke@posteo.de>
All rights reserved.

"""

import tempfile
from pyfingerprint.pyfingerprint import PyFingerprint
import sys, os


## Reads image and download it
##

## Tries to initialize the sensor
try:
    f = PyFingerprint('/dev/ttyUSB0', 57600, 0xFFFFFFFF, 0x00000000)

    if ( f.verifyPassword() == False ):
        raise ValueError('The given fingerprint sensor password is wrong!')

except Exception as e:
    print('The fingerprint sensor could not be initialized!')
    print('Exception message: ' + str(e))
    exit(1)

## Gets some sensor information
print('Currently used templates: ' + str(f.getTemplateCount()) +'/'+ str(f.getStorageCapacity()))

## Tries to read image and download it
try:
    print('Waiting for finger...')

    ## Wait that finger is read
    while ( f.readImage() == False ):
        pass

    #beep = lambda x: os.system("echo -n '\a';sleep 0.2;" * x)
    #beep(300)


    imageDestination =  os.path.dirname(os.path.abspath(sys.argv[0])) + '/fingerprint.bmp'
    print('Saving file to: ' + imageDestination)
    print('(This migth take a while...)')
    f.downloadImage(imageDestination)

    ##Converts taken finger image to characteristics and places them in temp buffer 0x01
    f.convertImage(0x01)

    ##Get charactersitcis from Sensor into computer.
    characterics = str(f.downloadCharacteristics(0x01))

    print('<output>' + imageDestination + ';' + characterics)
    
except Exception as e:
    print('Operation failed!')
    print('Exception message: ' + str(e))
    exit(1)
