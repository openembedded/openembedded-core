SECTION = "kernel"
DESCRIPTION = "Dummy Zylonite Linux kernel"
LICENSE = "GPL"

PROVIDES += "virtual/kernel"

PACKAGES_DYNAMIC += "kernel-module-*"
PACKAGES_DYNAMIC += "kernel-image-*"

COMPATIBLE_MACHINE = "zylonite"

PR = "r0"

SRC_URI = ""

do_configure() {
        :
}

do_compile () {
        :
}

do_install() {
        :
}

do_stage () {
	:
}
