include ipkg.inc
PR = "r5"

SRC_URI += "file://uninclude-replace.patch;patch=1 \
	file://uclibc.patch;patch=1"
