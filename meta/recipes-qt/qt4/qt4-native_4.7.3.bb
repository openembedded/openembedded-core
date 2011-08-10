require qt4-native.inc

PR = "${INC_PR}.1"

# Find the g++.conf/linux.conf in the right directory.
FILESEXTRAPATHS =. "${FILE_DIRNAME}/qt-${PV}:"

EXTRA_OECONF += " -no-fast -silent -no-rpath"

TOBUILD := "src/tools/bootstrap ${TOBUILD}"

SRC_URI[md5sum] = "49b96eefb1224cc529af6fe5608654fe"
SRC_URI[sha256sum] = "d02b6fd69d089c01f4a787aa18175d074ccaecf8980a5956e328c2991905937e"
