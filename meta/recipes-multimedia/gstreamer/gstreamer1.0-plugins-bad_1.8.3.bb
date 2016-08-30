include gstreamer1.0-plugins-bad.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=73a5855a8119deb017f5f13cf327095d \
                    file://COPYING.LIB;md5=21682e4e8fea52413fd26c60acb907e5 \
                    file://gst/tta/crc32.h;beginline=12;endline=29;md5=27db269c575d1e5317fffca2d33b3b50 \
                    file://gst/tta/filters.h;beginline=12;endline=29;md5=8a08270656f2f8ad7bb3655b83138e5a"

SRC_URI = " \
    http://gstreamer.freedesktop.org/src/gst-plugins-bad/gst-plugins-bad-${PV}.tar.xz \
    file://configure-allow-to-disable-libssh2.patch \
    file://fix-maybe-uninitialized-warnings-when-compiling-with-Os.patch \
    file://avoid-including-sys-poll.h-directly.patch \
    file://ensure-valid-sentinels-for-gst_structure_get-etc.patch \
    file://0001-gstreamer-gl.pc.in-don-t-append-GL_CFLAGS-to-CFLAGS.patch \
    file://0002-glplugin-enable-gldeinterlace-on-OpenGL-ES.patch \
    file://0003-glcolorconvert-implement-multiple-render-targets-for.patch \
    file://0004-glcolorconvert-don-t-use-the-predefined-variable-nam.patch \
    file://0005-glshader-add-glBindFragDataLocation.patch \
    file://0006-glcolorconvert-GLES3-deprecates-texture2D-and-it-doe.patch \
    file://0008-gl-implement-GstGLMemoryEGL.patch \
"
SRC_URI[md5sum] = "955281a43e98c5464563fa049e0a0911"
SRC_URI[sha256sum] = "7899fcb18e6a1af2888b19c90213af018a57d741c6e72ec56b133bc73ec8509b"

S = "${WORKDIR}/gst-plugins-bad-${PV}"

# over-ride the default hls PACKAGECONFIG in gstreamer1.0-plugins-bad.inc to
# pass an additional --with-hls-crypto=XXX option (new in 1.7.x) and switch HLS
# AES decryption from nettle to openssl (ie a shared dependency with dtls).
# This should move back to the common .inc once the main recipe updates to 1.8.x
PACKAGECONFIG[hls] = "--enable-hls --with-hls-crypto=openssl,--disable-hls,openssl"

# The tinyalsa plugin was added prior to the 1.7.2 release
# https://cgit.freedesktop.org/gstreamer/gst-plugins-bad/commit/?id=c8bd74fa9a81398f57d976c478d2043f30188684
PACKAGECONFIG[tinyalsa] = "--enable-tinyalsa,--disable-tinyalsa,tinyalsa"

# The vulkan based video sink plugin was added prior to the 1.7.2 release
# https://cgit.freedesktop.org/gstreamer/gst-plugins-bad/commit/?id=5de6dd9f40629562acf90e35e1fa58464d66617d
PACKAGECONFIG[vulkan] = "--enable-vulkan,--disable-vulkan,libxcb"

# The dependency-less netsim plugin was added prior to the 1.7.2 release
# https://cgit.freedesktop.org/gstreamer/gst-plugins-bad/commit/?id=e3f9e854f08e82bfab11182c5a2aa6f9a0c73cd5
EXTRA_OECONF += " \
    --enable-netsim \
"

# In 1.6.2, the "--enable-hls" configure option generated an installable package
# called "gstreamer1.0-plugins-bad-fragmented". In 1.7.1 that HLS plugin package
# has become "gstreamer1.0-plugins-bad-hls". See:
# http://cgit.freedesktop.org/gstreamer/gst-plugins-bad/commit/?id=efe62292a3d045126654d93239fdf4cc8e48ae08

PACKAGESPLITFUNCS_append = " handle_hls_rename "

python handle_hls_rename () {
    d.setVar('RPROVIDES_gstreamer1.0-plugins-bad-hls', 'gstreamer1.0-plugins-bad-fragmented')
    d.setVar('RREPLACES_gstreamer1.0-plugins-bad-hls', 'gstreamer1.0-plugins-bad-fragmented')
    d.setVar('RCONFLICTS_gstreamer1.0-plugins-bad-hls', 'gstreamer1.0-plugins-bad-fragmented')
}
