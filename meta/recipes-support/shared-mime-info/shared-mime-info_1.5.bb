require shared-mime-info.inc

SRC_URI += "file://parallelmake.patch \
	    file://install-data-hook.patch"

SRC_URI[md5sum] = "cc3e78d8bceaf2b361f62d67a8b4dda4"
SRC_URI[sha256sum] = "d6412840eb265bf36e61fd7b6fc6bea21b0f58cb22bed16f2ccccdd54bea4180"
