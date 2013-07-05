SUMMARY = "XvMC: X Video Motion Compensation extension library"

DESCRIPTION = "XvMC extends the X Video extension (Xv) and enables \
hardware rendered motion compensation support."

require xorg-lib-common.inc

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=0a207f08d4961489c55046c9a5e500da \
                    file://wrapper/XvMCWrapper.c;endline=26;md5=5151daa8172a3f1bb0cb0e0ff157d9de"

DEPENDS += "libxext libxv videoproto"

PE = "1"

XORG_PN = "libXvMC"

SRC_URI[md5sum] = "2e4014e9d55c430e307999a6b3dd256d"
SRC_URI[sha256sum] = "5e1a401efa433f959d41e17932b8c218c56b931348f494b8fa4656d7d798b204"
