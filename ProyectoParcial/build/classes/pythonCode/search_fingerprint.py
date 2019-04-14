#!/usr/bin/env python
# -*- coding: utf-8 -*-

"""
PyFingerprint
Copyright (C) 2015 Bastian Raschke <bastian.raschke@posteo.de>
All rights reserved.

"""

import hashlib
from hashlib import blake2b
from pyfingerprint.pyfingerprint import PyFingerprint
import sys
import ast

finger_template1  = ast.literal_eval(sys.argv[1])
finger_template2  = ast.literal_eval(sys.argv[2])


##print("Enconded Characteristics before uploading: " + str(finger_template1) + "\n\n")
##print("Enconded Characteristics before uploading: " + str(finger_template2) + "\n\n")

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

## Load the template to compare against.
if (f.deleteTemplate(0) == True ):
    print('Template deleted!')

#print("Uploading characteristics")
f.uploadCharacteristics(0x01, finger_template1)
f.uploadCharacteristics(0x02, finger_template2)
#print("Peak on the uploaded characteristics: " + str(f.downloadCharacteristics(0x02)))

## Gets some sensor information
print("Currently used templates: " + str(f.getTemplateCount()) +'/'+ str(f.getStorageCapacity()))


## Tries to search the finger and calculate hash
try:
#    print("Waiting for finger...\n")
#
#    ## Wait that finger is read
#    while ( f.readImage() == False ):
#        pass

    ## Converts read image to characteristics and stores it in charbuffer 1
 #   f.convertImage(0x01)

    print("Comparing uploaded caharacteristics...")
        ## Searchs template
    result = f.compareCharacteristics()
	    
    if(result >= 50):
        print('<output>Success' + ';' + str(result))
    else:
        print('<output>Failed' + ';' + str(result))
    ##result = f.searchTemplate()

    ##positionNumber = result[0]
    ##accuracyScore = result[1]

    ##if ( positionNumber == -1 ):
    ##    print("No match found!")
    ##    exit(0)
    ##else:
    ##    print("Found template at position #" + str(positionNumber))
    ##    print("The accuracy score is: " + str(accuracyScore))

    ## OPTIONAL stuff
    ##

    ## Loads the found template to charbuffer 1
    ##f.loadTemplate(positionNumber, 0x01)

    ## Downloads the characteristics of template loaded in charbuffer 1
    ##characterics = str(f.downloadCharacteristics(0x01)).encode('utf-8')

    ## Hashes characteristics of template
    ##print("SHA-2 hash of template: {" + hashlib.sha256(characterics).hexdigest() + "}")

except Exception as e:
    print("Operation failed!")
    print("Exception message: " + str(e))
    exit(1)
