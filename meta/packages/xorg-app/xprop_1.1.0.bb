require xorg-app-common.inc

DESCRIPTION = "utility to display window and font properties of an X server"

LIC_FILES_CHKSUM = "file://COPYING;md5=4641deddaa80fe7ca88e944e1fd94a94"

DEPENDS += " libxmu virtual/libx11"

PR = "r0"
PE = "1"
