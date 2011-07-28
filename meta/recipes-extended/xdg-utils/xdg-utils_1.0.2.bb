SUMMARY = "Basic desktop integration functions"

DESCRIPTION = "The xdg-utils package is a set of simple scripts that provide basic \
desktop integration functions for any Free Desktop, such as Linux. \
They are intended to provide a set of defacto standards. \
The following scripts are provided at this time: \
xdg-desktop-icon \     
xdg-desktop-menu \  
xdg-email \ 
xdg-icon-resource \
xdg-mime \       
xdg-open \     
xdg-screensaver \ 
"

PR="r0"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=a5367a90934098d6b05af3b746405014"

SRC_URI = "http://portland.freedesktop.org/download/${BPN}-${PV}.tgz"

inherit autotools

SRC_URI[md5sum] = "348a5b91dc66426505022c74a64b2940"
SRC_URI[sha256sum] = "21aeb7d16b2529b8d3975118f59eec09953e09f9a68d718159e98c90474b01ac"
