include ipkg.inc
PR = "r2"
SRC_URI += "file://depends.patch;patch=1 \
	file://uninclude-replace.patch;patch=1 \
	file://remove-c99isms.patch;patch=1 \
	file://uclibc.patch;patch=1"
