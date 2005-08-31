include ipkg.inc
PR = "r5"

SRC_URI = "${HANDHELDS_CVS};module=familiar/dist/ipkg;tag=${@'V' + bb.data.getVar('PV',d,1).replace('.', '-')} \
	file://buffer-overflow.patch;patch=1 \
	file://uninclude-replace.patch;patch=1 \
	file://uclibc.patch;patch=1"
