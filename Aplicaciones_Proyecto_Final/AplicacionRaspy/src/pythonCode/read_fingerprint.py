#!/usr/bin/env python
# -*- coding: utf-8 -*-

"""
PyFingerprint
Copyright (C) 2015 Bastian Raschke <bastian.raschke@posteo.de>
All rights reserved.

"""

import hashlib
from pyfingerprint.pyfingerprint import PyFingerprint
import sys
import ast


## Search for a finger
##

## Tries to initialize the sensor
try:
    f = PyFingerprint('/dev/ttyUSB0', 57600, 0xFFFFFFFF, 0x00000000)

    if ( f.verifyPassword() == False ):
        raise ValueError('The given fingerprint sensor password is wrong!')

except Exception as e:
    print("The fingerprint sensor could not be initialized!\n" + "Exception message: " + str(e) )
    exit(1)


## Tries to search the finger and calculate hash
try:
    print("Waiting for finger...\n")

    ## Wait that finger is read
    while ( f.readImage() == False ):
        pass

    ## Converts read image to characteristics and stores it in charbuffer 1
    f.convertImage(0x01)

     ##Get charactersitcis from Sensor into computer.
    characterics = str(f.downloadCharacteristics(0x01))

    print('<output>' + characterics)

except Exception as e:
    print("Operation failed!")
    print("Exception message: " + str(e))
    exit(1)
