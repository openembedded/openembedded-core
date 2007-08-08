require apt-native.inc
PR = "r1"

SRC_URI += "file://nodoc.patch;patch=1 \
            file://noconfigure.patch;patch=1"
