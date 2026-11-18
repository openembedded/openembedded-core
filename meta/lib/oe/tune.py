#
# Copyright OpenEmbedded Contributors
#
# SPDX-License-Identifier: GPL-2.0-only
#

# RISC-V profile name -> equivalent ISA string, i.e. the mandatory extensions of
# each profile as ratified in the RISC-V Profiles specification
# (https://github.com/riscv/riscv-profiles):
#   RVA20U64, RVA22U64: RVA20/RVA22 Profiles, version 1.0
#   RVA23U64: RVA23 Profiles, version 1.0
# plus the extensions those imply (e.g. zmmul, zaamo, zca, zve*).
#
# Do not regenerate these from compiler output (e.g. gcc's -march expansion);
# a newer compiler, or clang, may expand a profile differently. For example,
# gcc's rva22u64 leaves out zihpm, which the specification makes mandatory.
RISCV_PROFILES = {
    'rva20u64': 'rv64imafdc_ziccamoa_ziccif_zicclsm_ziccrse_zicntr_zicsr_zmmul_za128rs_zaamo_zalrsc_zca_zcd',
    'rva22u64': 'rv64imafdcb_zic64b_zicbom_zicbop_zicboz_ziccamoa_ziccif_zicclsm_ziccrse_zicntr_zicsr_zihintpause_zihpm_zmmul_za64rs_zaamo_zalrsc_zfhmin_zca_zcd_zba_zbb_zbs_zkt',
    'rva23u64': 'rv64imafdcbv_zic64b_zicbom_zicbop_zicboz_ziccamoa_ziccif_zicclsm_ziccrse_zicntr_zicond_zicsr_zihintntl_zihintpause_zihpm_zimop_zmmul_za64rs_zaamo_zalrsc_zawrs_zfa_zfhmin_zca_zcb_zcd_zcmop_zba_zbb_zbs_zkt_zvbb_zve32f_zve32x_zve64d_zve64f_zve64x_zvfhmin_zvkb_zvkt_zvl128b_zvl32b_zvl64b_supm',
}

# riscv_isa_to_tune(isa)
#
# Automatically translate a RISC-V ISA string to TUNE_FEATURES
#
# Abbreviations, such as rv32g -> rv32imafd_zicsr_zifencei are supported.
#
# The profile names in RISCV_PROFILES, such as rva22u64, are also supported,
# and are translated to the profile's mandatory extensions.
#
def riscv_isa_to_tune(isa):
    _isa = RISCV_PROFILES.get(isa.lower(), isa).lower()

    feature = []
    iter = 0

    # rv or riscv
    if _isa[iter:].startswith('rv'):
        feature.append('rv')
        iter = iter + 2
    elif _isa[iter:].startswith('riscv'):
        feature.append('rv')
        iter = iter + 5
    else:
        # Not a risc-v ISA!
        return _isa

    while (_isa[iter:]):
        # Skip _ and whitespace
        if _isa[iter] == '_' or _isa[iter].isspace():
            iter = iter + 1
            continue

        # Length, just capture numbers here
        if _isa[iter].isdigit():
            iter_end = iter
            while iter_end < len(_isa) and _isa[iter_end].isdigit():
                iter_end = iter_end + 1

            feature.append(_isa[iter:iter_end])
            iter = iter_end
            continue

        # Typically i, e or g is next, followed by extensions.
        # Extensions are single character, except for Z, Ss, Sh, Sm, Sv, and X

        # If the extension starts with 'Z', 'S' or 'X' use the name until the next _, whitespace or end
        if _isa[iter] in ['z', 's', 'x']:
            ext_type = _isa[iter]
            iter_end = iter + 1

            # Multicharacter extension, these are supposed to have a _ before the next multicharacter extension
            # See 37.4 and 37.5:
            # 37.4: Underscores "_" may be used to separate ISA extensions...
            # 37.5: All multi-letter extensions ... must be separated from other multi-letter extensions by an underscore...
            # Some extensions permit only alphabetic characters, while others allow alphanumeric chartacters
            while iter_end < len(_isa) and _isa[iter_end] != "_" and not _isa[iter_end].isspace():
                iter_end = iter_end + 1

            feature.append(_isa[iter:iter_end])
            iter = iter_end
            continue

        # 'g' is special, it's an abbreviation for imafd_zicsr_zifencei
        # When expanding the abbreviation, any additional letters must appear before the _z* extensions
        if _isa[iter] == 'g':
            _isa = 'imafd' + _isa[iter+1:] + '_zicsr_zifencei'
            iter = 0
            continue

        feature.append(_isa[iter])
        iter = iter + 1
        continue

    # Eliminate duplicates, but preserve the order
    feature = list(dict.fromkeys(feature))
    return ' '.join(feature)
