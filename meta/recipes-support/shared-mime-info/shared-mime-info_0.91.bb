require shared-mime-info.inc
PR = "r4"

SRC_URI[md5sum] = "982a211560ba4c47dc791ccff34e8fbc"
SRC_URI[sha256sum] = "98cfebe1d809afb24934e634373821e2a1dfa86fc6462cab230589a1c80988bd"

SRC_URI =+ "file://parallelmake.patch \
	    file://install-data-hook.patch"
