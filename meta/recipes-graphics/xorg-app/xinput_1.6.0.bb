require xorg-app-common.inc

SUMMARY = "Runtime configuration and test of XInput devices"

DESCRIPTION = "Xinput is an utility for configuring and testing XInput devices"

LIC_FILES_CHKSUM = "file://COPYING;md5=881525f89f99cad39c9832bcb72e6fa5"

DEPENDS += " libxi libxrandr libxinerama"

PR = "${INC_PR}.0"

SRC_URI[md5sum] = "d2459d35b4e0b41ded26a1d1159b7ac6"
SRC_URI[sha256sum] = "4ab007d952c76665603bcb82ceb15fd3929d10faf0580fc4873ac16f5f63847e"

