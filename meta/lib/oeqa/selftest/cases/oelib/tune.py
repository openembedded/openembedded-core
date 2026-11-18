#
# Copyright OpenEmbedded Contributors
#
# SPDX-License-Identifier: MIT
#

from unittest.case import TestCase
from oe.tune import RISCV_PROFILES, riscv_isa_to_tune

def _features(isa):
    return set(riscv_isa_to_tune(isa).split())

class TestRiscvIsaToTune(TestCase):
    def test_abbreviation(self):
        self.assertEqual(riscv_isa_to_tune('rv32g'), 'rv 32 i m a f d zicsr zifencei')
        self.assertEqual(riscv_isa_to_tune('rv64g'), 'rv 64 i m a f d zicsr zifencei')
        self.assertEqual(riscv_isa_to_tune('rv64gc'), 'rv 64 i m a f d c zicsr zifencei')
        self.assertEqual(riscv_isa_to_tune('rv32gcv'), 'rv 32 i m a f d c v zicsr zifencei')
        # Single-letter extensions after g stay ahead of the multi-letter ones
        self.assertEqual(riscv_isa_to_tune('rv64gcv_zba'), 'rv 64 i m a f d c v zba zicsr zifencei')

    def test_explicit(self):
        self.assertEqual(riscv_isa_to_tune('rv32imac_zicsr_zifencei'), 'rv 32 i m a c zicsr zifencei')
        self.assertEqual(riscv_isa_to_tune('rv32ec'), 'rv 32 e c')
        self.assertEqual(riscv_isa_to_tune('RV64IMAFD_Zicsr'), 'rv 64 i m a f d zicsr')
        # riscv as well as rv
        self.assertEqual(riscv_isa_to_tune('riscv64gc'), riscv_isa_to_tune('rv64gc'))

    def test_separators(self):
        self.assertEqual(riscv_isa_to_tune('rv64imac zicsr'), 'rv 64 i m a c zicsr')

    def test_duplicates(self):
        self.assertEqual(riscv_isa_to_tune('rv64imac_zicsr_zicsr'), 'rv 64 i m a c zicsr')

    def test_multi_letter_prefixes(self):
        # z, s and x extensions are all kept whole
        self.assertEqual(riscv_isa_to_tune('rv64gc_xtheadba_svpbmt'),
                         'rv 64 i m a f d c xtheadba svpbmt zicsr zifencei')

    def test_not_riscv(self):
        self.assertEqual(riscv_isa_to_tune('x86_64'), 'x86_64')
        self.assertEqual(riscv_isa_to_tune('aarch64'), 'aarch64')
        self.assertEqual(riscv_isa_to_tune(''), '')

    def test_profile(self):
        self.assertEqual(riscv_isa_to_tune('rva20u64'),
                         'rv 64 i m a f d c ziccamoa ziccif zicclsm ziccrse zicntr zicsr zmmul za128rs zaamo zalrsc zca zcd')
        for name, isa in RISCV_PROFILES.items():
            with self.subTest(profile=name):
                tune = riscv_isa_to_tune(name).split()
                self.assertEqual(tune, riscv_isa_to_tune(isa).split())
                self.assertEqual(riscv_isa_to_tune(name.upper()).split(), tune)
                self.assertEqual(tune[:3], ['rv', '64', 'i'])
                self.assertEqual(len(tune), len(set(tune)))
                # Required by Linux, but not by the profiles
                self.assertNotIn('zifencei', tune)

    def test_profile_sizes(self):
        self.assertEqual(len(_features('rva20u64')), 20)
        self.assertEqual(len(_features('rva22u64')), 32)
        self.assertEqual(len(_features('rva23u64')), 53)

    def test_rva22u64_additions(self):
        # Everything in RVA20U64 is in RVA22U64, except that the latter's za64rs
        # (a reservation set of at most 64 bytes) replaces za128rs
        rva20 = _features('rva20u64')
        rva22 = _features('rva22u64')
        self.assertEqual(rva20 - rva22, {'za128rs'})
        self.assertEqual(rva22 - rva20,
                         {'b', 'za64rs', 'zba', 'zbb', 'zbs', 'zfhmin', 'zic64b',
                          'zicbom', 'zicbop', 'zicboz', 'zihintpause', 'zihpm', 'zkt'})

    def test_rva23u64_additions(self):
        rva22 = _features('rva22u64')
        rva23 = _features('rva23u64')
        self.assertLessEqual(rva22, rva23)
        self.assertEqual(rva23 - rva22,
                         {'supm', 'v', 'zawrs', 'zcb', 'zcmop', 'zfa', 'zicond',
                          'zihintntl', 'zimop', 'zvbb', 'zve32f', 'zve32x',
                          'zve64d', 'zve64f', 'zve64x', 'zvfhmin', 'zvkb', 'zvkt',
                          'zvl128b', 'zvl32b', 'zvl64b'})
