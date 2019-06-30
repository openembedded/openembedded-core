SUMMARY = "Very high-quality data compression program"
DESCRIPTION = "bzip2 compresses files using the Burrows-Wheeler block-sorting text compression algorithm, and \
Huffman coding. Compression is generally considerably better than that achieved by more conventional \
LZ77/LZ78-based compressors, and approaches the performance of the PPM family of statistical compressors."
HOMEPAGE = "https://sourceware.org/bzip2/"
SECTION = "console/utils"
LICENSE = "bzip2"
LIC_FILES_CHKSUM = "file://LICENSE;beginline=4;endline=37;md5=39406315f540c69bd05b1531daedd2ae"

SRC_URI = "https://sourceware.org/pub/${BPN}/${BPN}-${PV}.tar.gz \
           file://configure.ac;subdir=${BP} \
           file://Makefile.am;subdir=${BP} \
           file://run-ptest \
           "
SRC_URI[md5sum] = "1a6a61cc867be4f3d6549037a09bf13e"
SRC_URI[sha256sum] = "e768a87c5b1a79511499beb41500bcc4caf203726fff46a6f5f9ad27fe08ab2b"

UPSTREAM_CHECK_URI = "https://www.sourceware.org/pub/bzip2/"
UPSTREAM_VERSION_UNKNOWN = "1"

PACKAGES =+ "libbz2"

CFLAGS_append = " -fPIC -fpic -Winline -fno-strength-reduce -D_FILE_OFFSET_BITS=64"

inherit autotools update-alternatives ptest relative_symlinks

ALTERNATIVE_PRIORITY = "100"
ALTERNATIVE_${PN} = "bunzip2 bzcat bzip2"

#install binaries to bzip2-native under sysroot for replacement-native
EXTRA_OECONF_append_class-native = " --bindir=${STAGING_BINDIR_NATIVE}/${PN}"

do_install_ptest () {
	sed -i -e "s|^Makefile:|_Makefile:|" ${D}${PTEST_PATH}/Makefile
}

FILES_libbz2 = "${libdir}/lib*${SOLIBS}"

RDEPENDS_${PN}-ptest += "make"

PROVIDES_append_class-native = " bzip2-replacement-native"
BBCLASSEXTEND = "native nativesdk"
