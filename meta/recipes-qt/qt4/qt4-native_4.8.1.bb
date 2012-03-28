require qt4-native.inc

PR = "${INC_PR}.0"

DEFAULT_PREFERENCE = "-1"

# Find the g++.conf/linux.conf in the right directory.
FILESEXTRAPATHS =. "${FILE_DIRNAME}/qt-${PV}:"

TOBUILD := "src/tools/bootstrap ${TOBUILD}"

SRC_URI[md5sum] = "7960ba8e18ca31f0c6e4895a312f92ff"
SRC_URI[sha256sum] = "ef851a36aa41b4ad7a3e4c96ca27eaed2a629a6d2fa06c20f072118caed87ae8"

