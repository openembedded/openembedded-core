SECTION = "libs"
PACKAGES = ""
PACKAGES_DYNAMIC = ""
PROVIDES = "virtual/${TARGET_PREFIX}libc-for-gcc"
DEPENDS = "virtual/${TARGET_PREFIX}gcc-initial linux-libc-headers"

do_install () {
	:
}

GLIBC_ADDONS = "nptl,ports"
GLIBC_EXTRA_OECONF = ""
