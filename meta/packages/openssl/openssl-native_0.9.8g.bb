inherit pkgconfig native

require openssl.inc

PR = "r1"

# This flag can contain target options (e.g -mfpu=neon for armv7-a systems)
export FULL_OPTIMIZATION = " "
export BUILD_OPTIMIZATION = " "

SRC_URI += "file://debian.patch;patch=1 \
            file://configure-targets.patch;patch=1 \
            file://shared-libs.patch;patch=1"

PARALLEL_MAKE = ""

FILESPATH = "${@base_set_filespath( ['${FILE_DIRNAME}/openssl-${PV}', '${FILE_DIRNAME}/openssl', '${FILE_DIRNAME}/files', '${FILE_DIRNAME}' ], d)}"

do_install() {
	:
}

