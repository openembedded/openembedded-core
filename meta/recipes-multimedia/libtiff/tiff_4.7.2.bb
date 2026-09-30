SUMMARY = "Provides support for the Tag Image File Format (TIFF)"
DESCRIPTION = "Library provides support for the Tag Image File Format \
(TIFF), a widely used format for storing image data.  This library \
provide means to easily access and create TIFF image files."
HOMEPAGE = "http://www.libtiff.org/"
LICENSE = "BSD-4.3TAHOE AND libtiff"
LIC_FILES_CHKSUM = "file://LICENSE.md;md5=4ab490c3088a0acff254eb2f8c577547"

CVE_PRODUCT = "libtiff"

SRC_URI = "http://download.osgeo.org/libtiff/tiff-${PV}.tar.gz \
           file://test_ifd_loop_detection-relative-images.patch \
           file://run-ptest \
	   "

SRC_URI[sha256sum] = "672bd7d10aee4606171afb864f3570b83340f6a33e2c186dc0512f7145ffdf6a"

# exclude betas
UPSTREAM_CHECK_REGEX = "tiff-(?P<pver>\d+(\.\d+)+).tar"

CVE_STATUS[CVE-2015-7313] = "fixed-version: Tested with check from https://security-tracker.debian.org/tracker/CVE-2015-7313 and already 4.3.0 doesn't have the issue"
CVE_STATUS[CVE-2023-52356] = "fixed-version: Fixed since 4.7.0, NVD tracks this as version-less vulnerability"
CVE_STATUS[CVE-2023-6228] = "fixed-version: Fixed since 4.7.0, NVD tracks this as version-less vulnerability"
CVE_STATUS[CVE-2023-6277] = "fixed-version: Fixed since 4.7.0, NVD tracks this as version-less vulnerability"
CVE_STATUS[CVE-2025-8851] = "fixed-version: Fixed since 4.7.0, NVD tracks this as fixed in 2024-08-11 vulnerability"
CVE_STATUS[CVE-2026-4775] = "fixed-version: Fixed since 4.7.2, NVD tracks this as version-less vulnerability"

inherit autotools multilib_header ptest

CACHED_CONFIGUREVARS = "ax_cv_check_gl_libgl=no"

PACKAGECONFIG ?= "cxx jpeg zlib lzma \
                  strip-chopping extrasample-as-alpha check-ycbcr-subsampling"

PACKAGECONFIG[cxx] = "--enable-cxx,--disable-cxx,,"
PACKAGECONFIG[jbig] = "--enable-jbig,--disable-jbig,jbig,"
PACKAGECONFIG[jpeg] = "--enable-jpeg,--disable-jpeg,jpeg,"
PACKAGECONFIG[lerc] = "--enable-lerc,--disable-lerc,liblerc,"
PACKAGECONFIG[zlib] = "--enable-zlib,--disable-zlib,zlib,"
PACKAGECONFIG[lzma] = "--enable-lzma,--disable-lzma,xz,"
PACKAGECONFIG[webp] = "--enable-webp,--disable-webp,libwebp,"
PACKAGECONFIG[zstd] = "--enable-zstd,--disable-zstd,zstd,"
PACKAGECONFIG[libdeflate] = "--enable-libdeflate,--disable-libdeflate,libdeflate,"

# Convert single-strip uncompressed images to multiple strips of specified
# size (default: 8192) to reduce memory usage
PACKAGECONFIG[strip-chopping] = "--enable-strip-chopping,--disable-strip-chopping,,"

# Treat a fourth sample with no EXTRASAMPLE_ value as being ASSOCALPHA
PACKAGECONFIG[extrasample-as-alpha] = "--enable-extrasample-as-alpha,--disable-extrasample-as-alpha,,"

# Control picking up YCbCr subsample info. Disable to support files lacking
# the tag
PACKAGECONFIG[check-ycbcr-subsampling] = "--enable-check-ycbcr-subsampling,--disable-check-ycbcr-subsampling,,"

# Support a mechanism allowing reading large strips (usually one strip files)
# in chunks when using TIFFReadScanline. Experimental 4.0+ feature
PACKAGECONFIG[chunky-strip-read] = "--enable-chunky-strip-read,--disable-chunky-strip-read,,"

PACKAGES =+ "tiffxx tiff-utils"
FILES:tiffxx = "${libdir}/libtiffxx.so.*"
FILES:tiff-utils = "${bindir}/*"

do_install:append() {
    oe_multilib_header tiffconf.h
}

BBCLASSEXTEND = "native nativesdk"

# C unit-test programs (check_PROGRAMS) built and run by ptest. Defined once
# here and substituted into run-ptest at install time to avoid duplication.
TIFF_PTEST_PROGS = "ascii_tag long_tag short_tag strip_rw rewrite custom_dir \
    custom_dir_EXIF_231 defer_strile_loading defer_strile_writing \
    test_directory test_IFD_enlargement test_open_options \
    test_append_to_strip test_ifd_loop_detection testtypes \
    test_signed_tags raw_decode"

do_compile_ptest() {
    oe_runmake -C ${B}/test check TESTS=""
}

do_install_ptest() {
    install -d ${D}${PTEST_PATH}/test
    # Compiled C unit-test programs. libtool leaves a wrapper script in test/
    # and the real ELF binary in test/.libs/; testtypes is static and lives
    # only in test/.
    for prog in ${TIFF_PTEST_PROGS}; do
        if [ -e ${B}/test/.libs/$prog ]; then
            install -m 0755 ${B}/test/.libs/$prog ${D}${PTEST_PATH}/test/
        else
            install -m 0755 ${B}/test/$prog ${D}${PTEST_PATH}/test/
        fi
    done
    # Shell test scripts and the shared helper
    install ${S}/test/*.sh ${D}${PTEST_PATH}/test/
    install ${S}/test/common.sh ${D}${PTEST_PATH}/test/
    # Point the test scripts at the installed tiff tools instead of ../tools
    sed -i -e "s|^TOOLS=.*|TOOLS=${bindir}|" ${D}${PTEST_PATH}/test/common.sh
    # Fill in the C test program list from TIFF_PTEST_PROGS.
    sed -i -e "s|@PROGS@|${TIFF_PTEST_PROGS}|" ${D}${PTEST_PATH}/run-ptest
    # Input images and reference outputs
    cp -r ${S}/test/images ${D}${PTEST_PATH}/test/
    cp -r ${S}/test/refs ${D}${PTEST_PATH}/test/
}

RDEPENDS:${PN}-ptest += "tiff-utils"
