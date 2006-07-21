include hostap-modules.inc
PR = "r10"

SRC_URI += "file://Makefile.patch;patch=1 \
	    file://add_event.patch;patch=1 \
	    file://hostap-utsname.patch;patch=1 \
	    file://hostap_cardid.patch;patch=1"
