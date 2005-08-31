#
# Meta package for maemo based system image
#

FEED_URIS_append_familiar   = " maemo##http://familiar.handhelds.org/releases/${DISTRO_VERSION}/feed/maemo"

PR = "r2"

export IMAGE_BASENAME = "maemo-image"

GUI_MACHINE_CLASS ?= "none"

MAEMO_EXTRA_DEPENDS = "scap dosfstools"
MAEMO_EXTRA_INSTALL = "osso-af-services osso-af-base-apps scap dosfstools"

XSERVER ?= "xserver-kdrive-omap"

DEPENDS = "task-bootstrap \
	   meta-maemo \
	   ${MAEMO_EXTRA_DEPENDS}"

export IPKG_INSTALL = "task-bootstrap maemo-task-base maemo-task-theme \
	               maemo-task-apps ${MAEMO_EXTRA_INSTALL} \
		       ${XSERVER}"

inherit image_ipk
LICENSE = MIT
