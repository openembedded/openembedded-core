HOMEPAGE = "https://github.com/KhronosGroup/SPIRV-LLVM-Translator"
SUMMARY = "LLVM/SPIR-V Bi-Directional Translator, a library and tool for translation between LLVM IR and SPIR-V."

LICENSE = "NCSA"
LIC_FILES_CHKSUM = "file://LICENSE.TXT;md5=47e311aa9caedd1b3abf098bd7814d1d"

# pattern: llvm_branch_200, currently there are no minor releases, so, no llvm_branch_201
SPIRV_BRANCH = "main"
SRCREV = "f821423888319dd2f0c436fc9ab86c798ac48ba1"
SRC_URI = " \
    git://github.com/KhronosGroup/SPIRV-LLVM-Translator;protocol=https;branch=${SPIRV_BRANCH} \
"

UPSTREAM_CHECK_GITTAGREGEX = "v(?P<pver>\d+(\.\d+)+)"

DEPENDS = "llvm spirv-tools spirv-headers"

inherit cmake pkgconfig lib_package

EXTRA_OECMAKE = "\
    -DBASE_LLVM_VERSION=22.1.0 \
    -DCMAKE_SKIP_BUILD_RPATH=ON \
    -DBUILD_SHARED_LIBS=ON \
    -DLLVM_EXTERNAL_SPIRV_HEADERS_SOURCE_DIR=${STAGING_INCDIR}/.. \
"

BBCLASSEXTEND = "native nativesdk"
