require iproute2.inc

PR = "r0"

SRC_URI = "http://developer.osdl.org/dev/iproute2/download/${BPN}-${PV}.tar.bz2 \
	   file://configure-cross.patch"

SRC_URI[md5sum] = "a243bfea837e71824b7ca26c3bb45fa8"
SRC_URI[sha256sum] = "47629a4f547f21d94d8e823a87dd8e13042cadecefea2e2dc433e4134fa9aec4"
