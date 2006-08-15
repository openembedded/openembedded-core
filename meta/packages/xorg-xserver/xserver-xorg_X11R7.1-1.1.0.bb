require xorg-xserver-common.inc

SRC_URI += "file://drmfix.patch;patch=1"

EXTRA_OECONF += " ac_cv_file__usr_share_X11_sgml_defs_ent=no "

#DESCRIPTION = ""

#DEPENDS += " "
