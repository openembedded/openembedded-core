SUMMARY = "Machine-readable files for the SPIR-V Registry"
SECTION = "graphics"
HOMEPAGE = "https://www.khronos.org/registry/spir-v"
LICENSE = "MIT & CC-BY-4.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=a0dcaa512cc2dee95fe0fd791ee83a18"

SRCREV = "01e0577914a75a2569c846778c2f93aa8e6feddd"
SRC_URI = "git://github.com/KhronosGroup/SPIRV-Headers;protocol=https;branch=vulkan-sdk-1.4.328 \
"
PE = "1"
# These recipes need to be updated in lockstep with each other:
# glslang, vulkan-headers, vulkan-loader, vulkan-tools, spirv-headers, spirv-tools
# vulkan-validation-layers, vulkan-utility-libraries, vulkan-volk.
# The tags versions should always be sdk-x.y.z, as this is what
# upstream considers a release.
UPSTREAM_CHECK_GITTAGREGEX = "sdk-(?P<pver>\d+(\.\d+)+)"

inherit cmake

BBCLASSEXTEND = "native nativesdk"
