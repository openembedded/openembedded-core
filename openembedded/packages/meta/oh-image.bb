PR = "r2"

export IMAGE_BASENAME = "oh-image"

GUI_MACHINE_CLASS ?= "none"

XSERVER ?= "xserver-kdrive-fbdev"

DEPENDS = "task-bootstrap \
	   meta-oh"

export IPKG_INSTALL = "task-bootstrap \
                       oh-task-base \
          	       ${XSERVER} "

#ROOTFS_POSTPROCESS_COMMAND += "zap_root_password; "

inherit image_ipk
LICENSE = MIT
