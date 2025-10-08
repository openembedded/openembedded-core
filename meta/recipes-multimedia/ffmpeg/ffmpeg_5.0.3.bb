SUMMARY = "A complete, cross-platform solution to record, convert and stream audio and video."
DESCRIPTION = "FFmpeg is the leading multimedia framework, able to decode, encode, transcode, \
               mux, demux, stream, filter and play pretty much anything that humans and machines \
               have created. It supports the most obscure ancient formats up to the cutting edge."
HOMEPAGE = "https://www.ffmpeg.org/"
SECTION = "libs"

LICENSE = "GPL-2.0-or-later & LGPL-2.1-or-later & ISC & MIT & BSD-2-Clause & BSD-3-Clause & IJG"
LICENSE:${PN} = "GPL-2.0-or-later"
LICENSE:libavcodec = "${@bb.utils.contains('PACKAGECONFIG', 'gpl', 'GPL-2.0-or-later', 'LGPL-2.1-or-later', d)}"
LICENSE:libavdevice = "${@bb.utils.contains('PACKAGECONFIG', 'gpl', 'GPL-2.0-or-later', 'LGPL-2.1-or-later', d)}"
LICENSE:libavfilter = "${@bb.utils.contains('PACKAGECONFIG', 'gpl', 'GPL-2.0-or-later', 'LGPL-2.1-or-later', d)}"
LICENSE:libavformat = "${@bb.utils.contains('PACKAGECONFIG', 'gpl', 'GPL-2.0-or-later', 'LGPL-2.1-or-later', d)}"
LICENSE:libavutil = "${@bb.utils.contains('PACKAGECONFIG', 'gpl', 'GPL-2.0-or-later', 'LGPL-2.1-or-later', d)}"
LICENSE:libpostproc = "GPL-2.0-or-later"
LICENSE:libswresample = "${@bb.utils.contains('PACKAGECONFIG', 'gpl', 'GPL-2.0-or-later', 'LGPL-2.1-or-later', d)}"
LICENSE:libswscale = "${@bb.utils.contains('PACKAGECONFIG', 'gpl', 'GPL-2.0-or-later', 'LGPL-2.1-or-later', d)}"
LICENSE_FLAGS = "commercial"

LIC_FILES_CHKSUM = "file://COPYING.GPLv2;md5=b234ee4d69f5fce4486a80fdaf4a4263 \
                    file://COPYING.GPLv3;md5=d32239bcb673463ab874e80d47fae504 \
                    file://COPYING.LGPLv2.1;md5=bd7a443320af8c812e4c18d1b79df004 \
                    file://COPYING.LGPLv3;md5=e6a600fd5e1d9cbde2d983680233ad02"

SRC_URI = "https://www.ffmpeg.org/releases/${BP}.tar.xz \
           file://0001-libavutil-include-assembly-with-full-path-from-sourc.patch \
           file://CVE-2024-32230.patch \
           file://CVE-2023-51793.patch \
           file://CVE-2023-50008.patch \
           file://CVE-2024-31582.patch \
           file://CVE-2024-31578.patch \
           file://CVE-2023-51794.patch \
           file://CVE-2023-51798.patch \
           file://CVE-2023-47342.patch \
           file://CVE-2023-50007.patch \
           file://CVE-2023-51796.patch \
           file://CVE-2024-7055.patch \
           file://CVE-2024-35366.patch \
           file://CVE-2024-35367.patch \
           file://CVE-2024-35368.patch \
           file://CVE-2025-0518.patch \
           file://CVE-2024-36613.patch \
           file://CVE-2024-36616.patch \
           file://CVE-2024-36617.patch \
           file://CVE-2024-36618.patch \
           file://CVE-2024-28661.patch \
           file://CVE-2024-35369.patch \
           file://CVE-2025-25473.patch \
           file://CVE-2025-22919.patch \
           file://CVE-2025-22921.patch \
           file://CVE-2025-7700.patch \
           file://CVE-2023-6602-CVE-2023-6604-CVE-2023-6605-0001.patch \
           file://CVE-2023-6602-CVE-2023-6604-CVE-2023-6605-0002.patch \
           file://CVE-2023-6602-CVE-2023-6604-CVE-2023-6605-0003.patch \
           file://CVE-2025-1594.patch \
          "

SRC_URI[sha256sum] = "04c70c377de233a4b217c2fdf76b19aeb225a287daeb2348bccd978c47b1a1db"

# CVE-2023-39018 issue belongs to ffmpeg-cli-wrapper (Java wrapper around the FFmpeg CLI)
# and not ffmepg itself.
# https://security-tracker.debian.org/tracker/CVE-2023-39018
# https://bugzilla.suse.com/show_bug.cgi?id=CVE-2023-39018
CVE_CHECK_IGNORE += "CVE-2023-39018"

# There is no release which is vulnerable to these CVEs
# These vulnerabilities are in new features being developed and fixed before releasing them
# feature (jpeg xl): https://github.com/FFmpeg/FFmpeg/commit/0c0dd23fe1102313742092c4760596971755814e
# bugfix: https://github.com/FFmpeg/FFmpeg/commit/bf814387f42e9b0dea9d75c03db4723c88e7d962
CVE_CHECK_IGNORE += "CVE-2023-46407"
# feature (evc parser): https://github.com/FFmpeg/FFmpeg/commit/34e4f18360c4ecb8e5979cab8f389478d8cd7819
# bugfix: https://github.com/FFmpeg/FFmpeg/commit/4565747056a11356210ed8edcecb920105e40b60
CVE_CHECK_IGNORE += "CVE-2023-47470"
# feature (jpeg xl): https://github.com/FFmpeg/FFmpeg/commit/0c0dd23fe1102313742092c4760596971755814e
# bugfix: https://github.com/FFmpeg/FFmpeg/commit/d2e8974699a9e35cc1a926bf74a972300d629cd5
CVE_CHECK_IGNORE += "CVE-2024-22860"
# feature (oqs audio decoder): https://github.com/FFmpeg/FFmpeg/commit/7ef9d31071021c05e6b792af3f25b7b9ceaa9258
# bugfix: https://github.com/FFmpeg/FFmpeg/commit/87b8c1081959e45ffdcbabb3d53ac9882ef2b5ce
CVE_CHECK_IGNORE += "CVE-2024-22861"
# feature (jpeg xl): https://github.com/FFmpeg/FFmpeg/commit/0c0dd23fe1102313742092c4760596971755814e
# bugfix: https://github.com/FFmpeg/FFmpeg/commit/ca09d8a0dcd82e3128e62463231296aaf63ae6f7
CVE_CHECK_IGNORE += "CVE-2024-22862"

# This vulnerability was introduced in 5.1 and fixed in 5.2 (backported also to 5.1.6), so 5.0.x is not affected
# introduced: https://github.com/FFmpeg/FFmpeg/commit/8a5896ec1f635ccf0d726f7ba7a06649ebeebf25
# bugfix: https://github.com/FFmpeg/FFmpeg/commit/9903ba28c28ab18dc7b7b6fb8571cc8b5caae1a6
CVE_CHECK_IGNORE += "CVE-2024-7272"

# Vulnerable code not present in any release
# introduced: https://git.ffmpeg.org/gitweb/ffmpeg.git/commit/19f7dae81ab2c19643b97da7556383ee3f721e78
# bugfix: https://git.ffmpeg.org/gitweb/ffmpeg.git/commit/43be8d07281caca2e88bfd8ee2333633e1fb1a13
CVE_CHECK_IGNORE += "CVE-2025-1373"

# This vulnerability was fixed in 5.0.2
# bugfix: https://git.ffmpeg.org/gitweb/ffmpeg.git/commit/3bc28e9d1ab33627cea3c632dd6b0c33e22e93ba
CVE_CHECK_IGNORE += "CVE-2022-48434"

# These two vulnerabilities were fixed in 5.0.3
# bugfix: https://git.ffmpeg.org/gitweb/ffmpeg.git/commit/2cdddcd6ec90c7a248ffe792d85faa4d89eab9f7
CVE_CHECK_IGNORE += "CVE-2022-3109"
# bugfix: https://git.ffmpeg.org/gitweb/ffmpeg.git/commit/481e81be1271ac9a0124ee615700390c2371bd89
CVE_CHECK_IGNORE += "CVE-2022-3341"

# This vulnerability was fixed in 5.0
# bugfix: https://github.com/FFmpeg/FFmpeg/commit/28c83584e8f3cd747c1476a74cc2841d3d1fa7f3
CVE_CHECK_IGNORE += "CVE-2023-6603"

# Build fails when thumb is enabled: https://bugzilla.yoctoproject.org/show_bug.cgi?id=7717
ARM_INSTRUCTION_SET:armv4 = "arm"
ARM_INSTRUCTION_SET:armv5 = "arm"
ARM_INSTRUCTION_SET:armv6 = "arm"

# Should be API compatible with libav (which was a fork of ffmpeg)
# libpostproc was previously packaged from a separate recipe
PROVIDES = "libav libpostproc"

DEPENDS = "nasm-native"

inherit autotools pkgconfig

PACKAGECONFIG ??= "avdevice avfilter avcodec avformat swresample swscale postproc \
                   alsa bzlib lzma pic pthreads shared theora zlib \
                   ${@bb.utils.contains('AVAILTUNES', 'mips32r2', 'mips32r2', '', d)} \
                   ${@bb.utils.contains('DISTRO_FEATURES', 'x11', 'xv xcb', '', d)}"

# libraries to build in addition to avutil
PACKAGECONFIG[avdevice] = "--enable-avdevice,--disable-avdevice"
PACKAGECONFIG[avfilter] = "--enable-avfilter,--disable-avfilter"
PACKAGECONFIG[avcodec] = "--enable-avcodec,--disable-avcodec"
PACKAGECONFIG[avformat] = "--enable-avformat,--disable-avformat"
PACKAGECONFIG[swresample] = "--enable-swresample,--disable-swresample"
PACKAGECONFIG[swscale] = "--enable-swscale,--disable-swscale"
PACKAGECONFIG[postproc] = "--enable-postproc,--disable-postproc"

# features to support
PACKAGECONFIG[alsa] = "--enable-alsa,--disable-alsa,alsa-lib"
PACKAGECONFIG[altivec] = "--enable-altivec,--disable-altivec,"
PACKAGECONFIG[bzlib] = "--enable-bzlib,--disable-bzlib,bzip2"
PACKAGECONFIG[fdk-aac] = "--enable-libfdk-aac --enable-nonfree,--disable-libfdk-aac,fdk-aac"
PACKAGECONFIG[gpl] = "--enable-gpl,--disable-gpl"
PACKAGECONFIG[gsm] = "--enable-libgsm,--disable-libgsm,libgsm"
PACKAGECONFIG[jack] = "--enable-indev=jack,--disable-indev=jack,jack"
PACKAGECONFIG[libopus] = "--enable-libopus,--disable-libopus,libopus"
PACKAGECONFIG[libvorbis] = "--enable-libvorbis,--disable-libvorbis,libvorbis"
PACKAGECONFIG[lzma] = "--enable-lzma,--disable-lzma,xz"
PACKAGECONFIG[mfx] = "--enable-libmfx,--disable-libmfx,intel-mediasdk"
PACKAGECONFIG[mp3lame] = "--enable-libmp3lame,--disable-libmp3lame,lame"
PACKAGECONFIG[openssl] = "--enable-openssl,--disable-openssl,openssl"
PACKAGECONFIG[sdl2] = "--enable-sdl2,--disable-sdl2,virtual/libsdl2"
PACKAGECONFIG[speex] = "--enable-libspeex,--disable-libspeex,speex"
PACKAGECONFIG[srt] = "--enable-libsrt,--disable-libsrt,srt"
PACKAGECONFIG[theora] = "--enable-libtheora,--disable-libtheora,libtheora libogg"
PACKAGECONFIG[vaapi] = "--enable-vaapi,--disable-vaapi,libva"
PACKAGECONFIG[vdpau] = "--enable-vdpau,--disable-vdpau,libvdpau"
PACKAGECONFIG[vpx] = "--enable-libvpx,--disable-libvpx,libvpx"
PACKAGECONFIG[x264] = "--enable-libx264,--disable-libx264,x264"
PACKAGECONFIG[x265] = "--enable-libx265,--disable-libx265,x265"
PACKAGECONFIG[xcb] = "--enable-libxcb,--disable-libxcb,libxcb"
PACKAGECONFIG[xv] = "--enable-outdev=xv,--disable-outdev=xv,libxv"
PACKAGECONFIG[zlib] = "--enable-zlib,--disable-zlib,zlib"

# other configuration options
PACKAGECONFIG[mips32r2] = ",--disable-mipsdsp --disable-mipsdspr2"
PACKAGECONFIG[pic] = "--enable-pic"
PACKAGECONFIG[pthreads] = "--enable-pthreads,--disable-pthreads"
PACKAGECONFIG[shared] = "--enable-shared"
PACKAGECONFIG[strip] = ",--disable-stripping"

# Check codecs that require --enable-nonfree
USE_NONFREE = "${@bb.utils.contains_any('PACKAGECONFIG', [ 'openssl' ], 'yes', '', d)}"

def cpu(d):
    for arg in (d.getVar('TUNE_CCARGS') or '').split():
        if arg.startswith('-mcpu='):
            return arg[6:]
    return 'generic'

EXTRA_OECONF = " \
    ${@bb.utils.contains('USE_NONFREE', 'yes', '--enable-nonfree', '', d)} \
    \
    --cross-prefix=${TARGET_PREFIX} \
    \
    --ld='${CCLD}' \
    --cc='${CC}' \
    --cxx='${CXX}' \
    --arch=${TARGET_ARCH} \
    --target-os='linux' \
    --enable-cross-compile \
    --extra-cflags='${CFLAGS} ${HOST_CC_ARCH}${TOOLCHAIN_OPTIONS}' \
    --extra-ldflags='${LDFLAGS}' \
    --sysroot='${STAGING_DIR_TARGET}' \
    ${EXTRA_FFCONF} \
    --libdir=${libdir} \
    --shlibdir=${libdir} \
    --datadir=${datadir}/ffmpeg \
    --cpu=${@cpu(d)} \
    --pkg-config=pkg-config \
"

EXTRA_OECONF:append:linux-gnux32 = " --disable-asm"

EXTRA_OECONF += "${@bb.utils.contains('TUNE_FEATURES', 'mipsisa64r6', '--disable-mips64r2 --disable-mips32r2', '', d)}"
EXTRA_OECONF += "${@bb.utils.contains('TUNE_FEATURES', 'mipsisa64r2', '--disable-mips64r6 --disable-mips32r6', '', d)}"
EXTRA_OECONF += "${@bb.utils.contains('TUNE_FEATURES', 'mips32r2', '--disable-mips64r6 --disable-mips32r6', '', d)}"
EXTRA_OECONF += "${@bb.utils.contains('TUNE_FEATURES', 'mips32r6', '--disable-mips64r2 --disable-mips32r2', '', d)}"
EXTRA_OECONF:append:mips = " --extra-libs=-latomic --disable-mips32r5 --disable-mipsdsp --disable-mipsdspr2 \
                             --disable-loongson2 --disable-loongson3 --disable-mmi --disable-msa"
EXTRA_OECONF:append:riscv32 = " --extra-libs=-latomic"
EXTRA_OECONF:append:armv5 = " --extra-libs=-latomic"
EXTRA_OECONF:append:powerpc = " --extra-libs=-latomic"

# gold crashes on x86, another solution is to --disable-asm but thats more hacky
# ld.gold: internal error in relocate_section, at ../../gold/i386.cc:3684

LDFLAGS:append:x86 = "${@bb.utils.contains('DISTRO_FEATURES', 'ld-is-gold', ' -fuse-ld=bfd ', '', d)}"

EXTRA_OEMAKE = "V=1"

do_configure() {
    ${S}/configure ${EXTRA_OECONF}
}

# patch out build host paths for reproducibility
do_compile:prepend:class-target() {
        sed -i -e "s,${WORKDIR},,g" ${B}/config.h
}

PACKAGES =+ "libavcodec \
             libavdevice \
             libavfilter \
             libavformat \
             libavutil \
             libpostproc \
             libswresample \
             libswscale"

FILES:libavcodec = "${libdir}/libavcodec${SOLIBS}"
FILES:libavdevice = "${libdir}/libavdevice${SOLIBS}"
FILES:libavfilter = "${libdir}/libavfilter${SOLIBS}"
FILES:libavformat = "${libdir}/libavformat${SOLIBS}"
FILES:libavutil = "${libdir}/libavutil${SOLIBS}"
FILES:libpostproc = "${libdir}/libpostproc${SOLIBS}"
FILES:libswresample = "${libdir}/libswresample${SOLIBS}"
FILES:libswscale = "${libdir}/libswscale${SOLIBS}"

# ffmpeg disables PIC on some platforms (e.g. x86-32)
INSANE_SKIP:${MLPREFIX}libavcodec = "textrel"
INSANE_SKIP:${MLPREFIX}libavdevice = "textrel"
INSANE_SKIP:${MLPREFIX}libavfilter = "textrel"
INSANE_SKIP:${MLPREFIX}libavformat = "textrel"
INSANE_SKIP:${MLPREFIX}libavutil = "textrel"
INSANE_SKIP:${MLPREFIX}libswscale = "textrel"
INSANE_SKIP:${MLPREFIX}libswresample = "textrel"
INSANE_SKIP:${MLPREFIX}libpostproc = "textrel"

CVE_PRODUCT = "ffmpeg libswresample libavcodec"
