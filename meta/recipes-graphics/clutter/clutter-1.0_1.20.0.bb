require clutter-1.0.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=4fbd65380cdd255951079008b364516c"

SRC_URI[archive.md5sum] = "a8a33a57a944c6d7c7c013ce9aa3222b"
SRC_URI[archive.sha256sum] = "cc940809e6e1469ce349c4bddb0cbcc2c13c087d4fc15cda9278d855ee2d1293"

SRC_URI += "file://install-examples.patch \
            file://run-installed-tests-with-tap-output.patch \
            file://run-ptest"
