# check src/llvm-project/llvm/CMakeLists.txt for llvm version in use
#
LLVM_RELEASE = "11.0.1"
require rust-source-${PV}.inc
require rust-llvm.inc

SRC_URI += "file://0001-nfc-Fix-missing-include.patch;striplevel=2" 