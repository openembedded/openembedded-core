FILESPATH := "${FILESPATH}:${@os.path.dirname(bb.data.getVar('FILE', d, True))}"
SRC_URI += "file://defconfig"
COMPATIBLE_MACHINE_emenlow = "emenlow"
