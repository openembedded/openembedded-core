require shared-mime-info.inc

SRC_URI[md5sum] = "743720bc4803dd69f55449013d350f31"
SRC_URI[sha256sum] = "4fd49c8c7ca9ecb10c59845094a18dbb73b69c72b4bad3db5e864f2111cb323a"

SRC_URI =+ "file://parallelmake.patch \
	    file://install-data-hook.patch"
