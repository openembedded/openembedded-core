# Copyright (C) 2017 Khem Raj <raj.khem@gmail.com>
# Released under the MIT license (see COPYING.MIT for the terms)

SUMMARY = "The LLVM Compiler Infrastructure"
HOMEPAGE = "http://llvm.org"
LICENSE = "Apache-2.0-with-LLVM-exception"
SECTION = "devel"

LIC_FILES_CHKSUM = "file://LICENSE.TXT;md5=8a15a0759ef07f2682d2ba4b893c9afe"

DEPENDS = "libffi libxml2 zlib zstd libedit ninja-native llvm-native"

RDEPENDS:${PN}:append:class-target = " ncurses-terminfo"

inherit cmake pkgconfig
# could be 'rcX' or 'git' or empty ( for release )
VER_SUFFIX = ""

PV .= "${VER_SUFFIX}"

MAJOR_VERSION = "${@oe.utils.trim_version("${PV}", 1)}"
MAJ_MIN_VERSION = "${@oe.utils.trim_version("${PV}", 2)}"

LLVM_RELEASE = "${PV}"

SRCREV_spirv = "68edc9d3d10ff6ec6353803a1bc60a5c25e7b715"
# pattern: llvm_branch_200, currently there are no minor releases, so, no llvm_branch_201
SPIRV_BRANCH = "llvm_release_${@oe.utils.trim_version('${PV}', 1).replace('.', '')}0"

SRC_URI_SPIRV = " \
    git://github.com/KhronosGroup/SPIRV-LLVM-Translator;protocol=https;name=spirv;branch=${SPIRV_BRANCH};destsuffix=llvm-project-${PV}.src/llvm/projects/SPIRV-LLVM-Translator \
    file://spirv-internal-build.patch \
"

SRC_URI_SPIRV:append:class-native = " \
    file://spirv-shared-library.patch \
"

SRC_URI = "https://github.com/llvm/llvm-project/releases/download/llvmorg-${PV}/llvm-project-${PV}.src.tar.xz \
           file://0007-llvm-allow-env-override-of-exe-path.patch \
           file://0001-AsmMatcherEmitter-sort-ClassInfo-lists-by-name-as-we.patch \
           ${@bb.utils.contains('PACKAGECONFIG', 'spirv-llvm-translator', '${SRC_URI_SPIRV}', '', d)} \
           file://llvm-config \
           "
SRC_URI[sha256sum] = "f0a4a240aabc9b056142d14d5478bb6d962aeac549cbd75b809f5499240a8b38"
UPSTREAM_CHECK_URI = "https://github.com/llvm/llvm-project"
UPSTREAM_CHECK_REGEX = "llvmorg-(?P<pver>\d+(\.\d+)+)"

SRC_URI:append:class-native = " file://0001-llvm-config-remove-LLVM_LDFLAGS-from-ldflags-output.patch"

S = "${WORKDIR}/llvm-project-${PV}.src"

OECMAKE_SOURCEPATH = "${S}/llvm"

LLVM_INSTALL_DIR = "${WORKDIR}/llvm-install"

PACKAGECONFIG ??= "libllvm libclc spirv-llvm-translator"
# if optviewer OFF, force the modules to be not found or the ones on the host would be found
PACKAGECONFIG[optviewer] = ",-DPY_PYGMENTS_FOUND=OFF -DPY_PYGMENTS_LEXERS_C_CPP_FOUND=OFF -DPY_YAML_FOUND=OFF,python3-pygments python3-pyyaml,python3-pygments python3-pyyaml"
PACKAGECONFIG[libllvm] = ""
PACKAGECONFIG[libclc] = ""
PACKAGECONFIG[spirv-llvm-translator] = "-DLLVM_EXTERNAL_SPIRV_HEADERS_SOURCE_DIR=${STAGING_INCDIR}/.. ,,spirv-tools-native spirv-headers"

#
# Default to build all OE-Core supported target arches (user overridable).
#
LLVM_TARGETS ?= "AMDGPU;AArch64;ARM;BPF;Mips;PowerPC;RISCV;X86;LoongArch;NVPTX;SPIRV"

ARM_INSTRUCTION_SET:armv5 = "arm"
ARM_INSTRUCTION_SET:armv4t = "arm"

LLVM_PROJECTS_CLANG = "${@bb.utils.contains_any('PACKAGECONFIG', 'libclc spirv-llvm-translator', 'clang', '', d)}"
LLVM_PROJECTS_CLC = "${@bb.utils.contains('PACKAGECONFIG', 'libclc', ';libclc', '', d)}"
LLVM_PROJECTS = "${LLVM_PROJECTS_CLANG}${LLVM_PROJECTS_CLC}"

EXTRA_OECMAKE += "-DLLVM_ENABLE_ASSERTIONS=OFF \
                  -DLLVM_ENABLE_EXPENSIVE_CHECKS=OFF \
                  -DLLVM_ENABLE_PIC=ON \
                  -DLLVM_BINDINGS_LIST='' \
                  -DLLVM_LINK_LLVM_DYLIB=ON \
                  -DLLVM_ENABLE_FFI=ON \
                  -DLLVM_ENABLE_RTTI=ON \
                  -DFFI_INCLUDE_DIR=$(pkg-config --variable=includedir libffi) \
                  -DLLVM_OPTIMIZED_TABLEGEN=ON \
                  -DLLVM_TARGETS_TO_BUILD='${LLVM_TARGETS}' \
                  -DLLVM_VERSION_SUFFIX='${VER_SUFFIX}' \
                  -DLLVM_TEMPORARILY_ALLOW_OLD_TOOLCHAIN=ON \
                  -DCMAKE_BUILD_TYPE=Release \
                  -DLLVM_ENABLE_PROJECTS='${LLVM_PROJECTS}' \
                 "

EXTRA_OECMAKE:append:class-target = "\
                  -DCMAKE_CROSSCOMPILING:BOOL=ON \
                  -DLLVM_HOST_TRIPLE=${TARGET_SYS} \
                  -DLLVM_TABLEGEN=${STAGING_BINDIR_NATIVE}/llvm-tblgen${PV} \
                  -DLLVM_CONFIG_PATH=${STAGING_BINDIR_NATIVE}/llvm-config${PV} \
                  -DLLVM_NATIVE_TOOL_DIR=${STAGING_BINDIR_NATIVE} \
                 "

EXTRA_OECMAKE:append:class-nativesdk = "\
                  -DCMAKE_CROSSCOMPILING:BOOL=ON \
                  -DLLVM_HOST_TRIPLE=${SDK_SYS} \
                  -DLLVM_TABLEGEN=${STAGING_BINDIR_NATIVE}/llvm-tblgen${PV} \
                  -DLLVM_CONFIG_PATH=${STAGING_BINDIR_NATIVE}/llvm-config${PV} \
                  -DLLVM_NATIVE_TOOL_DIR=${STAGING_BINDIR_NATIVE} \
                 "

# patch out build host paths for reproducibility
do_compile:prepend:class-target() {
        sed -i -e "s,${WORKDIR},,g" ${B}/tools/llvm-config/BuildVariables.inc
}

do_compile:prepend:class-nativesdk() {
        sed -i -e "s,${WORKDIR},,g" ${B}/tools/llvm-config/BuildVariables.inc
}

do_compile() {
    if ${@bb.utils.contains('PACKAGECONFIG', 'libllvm', 'true', 'false', d)}; then
	ninja -v ${PARALLEL_MAKE}
    else
	ninja -v ${PARALLEL_MAKE} llvm-config llvm-tblgen
    fi
}

do_install() {
    if ${@bb.utils.contains('PACKAGECONFIG', 'libllvm', 'true', 'false', d)}; then
	DESTDIR=${D} ninja -v install

        # llvm harcodes usr/lib as install path, so this corrects it to actual libdir
        mv -T -n ${D}/${prefix}/lib ${D}/${libdir} || true

        # Remove opt-viewer: https://llvm.org/docs/Remarks.html
        rm -rf ${D}${datadir}/opt-viewer

        # reproducibility
        sed -i -e 's,${WORKDIR},,g' ${D}/${libdir}/cmake/llvm/LLVMConfig.cmake
    fi

    # Remove clang bits from target packages, we are not providing it for the system
    if ${@bb.utils.contains_any('PACKAGECONFIG', 'libclc spirv-llvm-translator', 'true', 'false', d)} &&
       [ "${CLASSOVERRIDE}" != "class-native" ] ; then
        rm -f  ${D}${bindir}/clang*
        rm -fr ${D}${libdir}/clang
        rm -fr ${D}${datadir}/clang

        rm -f  ${D}${bindir}/scan*
        rm -fr ${D}${libdir}/libscanbuild
        rm -fr ${D}${datadir}/scan-build
        rm -fr ${D}${datadir}/scan-view

        rm -fr ${D}${libdir}/libear
    fi

    # Try to clean up datadir if it is empty, but don't fail if there are
    # libclc files there
    rmdir ${D}${datadir} || true
}

do_install:append:class-native() {
	install -D -m 0755 ${B}/bin/llvm-tblgen ${D}${bindir}/llvm-tblgen${PV}
	install -D -m 0755 ${B}/bin/llvm-config ${D}${bindir}/llvm-config${PV}
	ln -sf llvm-config${PV} ${D}${bindir}/llvm-config

        if ${@bb.utils.contains('PACKAGECONFIG', 'libclc', 'true', 'false', d)} ; then
            install -D -m 0755 ${B}/bin/prepare_builtins ${D}${bindir}/prepare_builtins
        fi
}

SYSROOT_PREPROCESS_FUNCS:append:class-target = " llvm_sysroot_preprocess"
SYSROOT_PREPROCESS_FUNCS:append:class-nativesdk = " llvm_sysroot_preprocess"

llvm_sysroot_preprocess() {
	install -d ${SYSROOT_DESTDIR}${bindir_crossscripts}/
	install -m 0755 ${UNPACKDIR}/llvm-config ${SYSROOT_DESTDIR}${bindir_crossscripts}/
	ln -sf llvm-config ${SYSROOT_DESTDIR}${bindir_crossscripts}/llvm-config${PV}
}

PACKAGES =+ "${PN}-bugpointpasses ${PN}-llvmhello ${PN}-libllvm ${PN}-liboptremarks ${PN}-liblto ${PN}-clc ${PN}-spirv"
PROVIDES = "${@bb.utils.filter('PACKAGECONFIG', 'libclc spirv-llvm-translator', d)}"

RRECOMMENDS:${PN}-dev += "${PN}-bugpointpasses ${PN}-llvmhello ${PN}-liboptremarks"
RPROVIDES:${PN}-clc = "${@bb.utils.filter('PACKAGECONFIG', 'libclc', d)}"
RPROVIDES:${PN}-spirv = "${@bb.utils.filter('PACKAGECONFIG', 'spirv-llvm-translator', d)}"

FILES:${PN}-bugpointpasses = "\
    ${libdir}/BugpointPasses.so \
"

FILES:${PN}-libllvm = "\
    ${libdir}/libLLVM-${MAJOR_VERSION}.so \
    ${libdir}/libLLVM.so.${MAJ_MIN_VERSION} \
"

FILES:${PN}-liblto += "\
    ${libdir}/libLTO.so.* \
"

FILES:${PN}-liboptremarks += "\
    ${libdir}/libRemarks.so.* \
"

FILES:${PN}-llvmhello = "\
    ${libdir}/LLVMHello.so \
"

FILES:${PN}-dev += " \
    ${libdir}/llvm-config \
    ${libdir}/libRemarks.so \
    ${libdir}/libLLVM-${PV}.so \
"

FILES:${PN}-staticdev += "\
    ${libdir}/*.a \
"

FILES:${PN}-clc += "${datadir}/clc"

FILES:${PN}-spirv = " \
    ${bindir}/llvm-spirv \
    ${includedir}/LLVMSPIRVLib \
    ${libdir}/pkgconfig/LLVMSPIRVLib.pc \
    ${libdir}/libLLVMSPIRV* \
"

INSANE_SKIP:${PN}-libllvm += "dev-so"

# SPIRV-LLVM-Translator provides only static libraries, they are included into
# the llvm-spirv package.
INSANE_SKIP:${PN}-spirv += "staticdev"

BBCLASSEXTEND = "native nativesdk"
