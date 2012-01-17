require m4.inc

LICENSE = "GPLv3"

LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504\
	            file://examples/COPYING;md5=fbc986d45b3dae6725c29870dd6b669d"

PR = "r2"
SRC_URI += "file://ac_config_links.patch"

SRC_URI[md5sum] = "a5dfb4f2b7370e9d34293d23fd09b280"
SRC_URI[sha256sum] = "e9176a35bb13a1b08482359aa554ee8072794f58f00e4827bf0e06b570c827da"


