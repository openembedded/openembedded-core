# Copyright (C) 2017 Intel Corporation
#
# SPDX-License-Identifier: MIT
#

import os
import sys
import glob
import re

from oeqa.core.context import OETestContext
from checklayer import get_signatures

class CheckLayerTestContext(OETestContext):
    def __init__(self, td=None, logger=None, layer=None, test_software_layer_signatures=True):
        super(CheckLayerTestContext, self).__init__(td, logger)
        self.layer = layer
        self.test_software_layer_signatures = test_software_layer_signatures
        self._world_runs = {}

    def _world_run(self, machine):
        '''
        Calls get_signatures(failsafe=False) once per machine and remembers
        the outcome: the signatures, or the RuntimeError raised instead.
        '''
        if machine not in self._world_runs:
            try:
                self._world_runs[machine] = get_signatures(self.td['builddir'], failsafe=False, machine=machine)
            except RuntimeError as ex:
                self._world_runs[machine] = ex
        return self._world_runs[machine]

    def world_error(self, machine):
        '''
        Returns the RuntimeError that get_signatures(failsafe=False) raises
        for the given machine, or None if world can be built.
        '''
        result = self._world_run(machine)
        if isinstance(result, RuntimeError):
            return result
        return None

    def world_signatures(self, machine):
        '''
        Returns (sigs, tune2tasks) like get_signatures(failsafe=True). When
        world can be built, -k makes no difference, so the signatures of the
        run without it are reused; otherwise bitbake runs again with -k.
        '''
        result = self._world_run(machine)
        if isinstance(result, RuntimeError):
            return get_signatures(self.td['builddir'], failsafe=True, machine=machine)
        return result
