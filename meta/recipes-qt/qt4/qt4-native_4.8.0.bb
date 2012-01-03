require qt4-native.inc

PR = "${INC_PR}.0"

DEFAULT_PREFERENCE = "-1"

# Find the g++.conf/linux.conf in the right directory.
FILESEXTRAPATHS =. "${FILE_DIRNAME}/qt-${PV}:"

TOBUILD := "src/tools/bootstrap ${TOBUILD}"

SRC_URI[md5sum] = "e8a5fdbeba2927c948d9f477a6abe904"
SRC_URI[sha256sum] = "9392b74e485e15f75a3e07a527547d4f6747eaf55ebce71ba0e863a9fd320b6e"

