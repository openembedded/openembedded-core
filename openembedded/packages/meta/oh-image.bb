PR = "r0"

export IMAGE_BASENAME = "oh-image"

GUI_MACHINE_CLASS ?= "none"

XSERVER ?= "xserver-kdrive-fbdev"

DEPENDS = "task-bootstrap \
	   meta-oh"

export IPKG_INSTALL = "task-bootstrap \
          	       ${XSERVER} "
#oh-task-base \

ROOTFS_POSTPROCESS_COMMAND += "zap_root_password; "

inherit image_ipk
LICENSE = MIT
