require apt-native.inc
PR = "r2"

SRC_URI += "file://nodoc.patch;patch=1 \
            file://noconfigure.patch;patch=1"
