SUMMARY = "Machine-readable files for the SPIR-V Registry"
SECTION = "graphics"
HOMEPAGE = "https://www.khronos.org/registry/spir-v"
LICENSE = "MIT & CC-BY-4.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=a0dcaa512cc2dee95fe0fd791ee83a18"

SRCREV = "2a611a970fdbc41ac2e3e328802aed9985352dca"
SRC_URI = "git://github.com/KhronosGroup/SPIRV-Headers;protocol=https;branch=main \
           file://0001-Add-SPV_INTEL_function_variants-532.patch \
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
