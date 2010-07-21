FILESPATH := "${FILESPATH}:${@os.path.dirname(bb.data.getVar('FILE', d, True))}"
