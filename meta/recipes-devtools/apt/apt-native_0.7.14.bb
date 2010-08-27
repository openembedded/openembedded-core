require apt-native.inc

PR = "r3"

SRC_URI += "file://nodoc.patch \
            file://noconfigure.patch \
	    file://no-curl.patch \
	    file://includes-fix.patch"
