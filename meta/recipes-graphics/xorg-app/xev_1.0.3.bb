require xorg-app-common.inc
LIC_FILES_CHKSUM = "file://xev.c;startline=0;endline=35;md5=db2e69260ca64f1d332efa6ab4b7e2b5"
DESCRIPTION = "X Event Viewer"
LICENSE = "MIT"
PE = "1"

SRC_URI += "file://diet-x11.patch"

SRC_URI[md5sum] = "a9532c3d1683c99bb5df1895cb3a60b1"
SRC_URI[sha256sum] = "d4ac7ae154ee9733be27a5f55586abb9362c768f5fb8a4fc7fd2645100a9313a"
