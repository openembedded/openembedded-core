# Copyright (C) 2016 Intel Corporation
# Released under the MIT license (see COPYING.MIT)

import os
from oeqa.sdk.context import OESDKTestContext, OESDKTestContextExecutor

class OESDKExtTestContext(OESDKTestContext):
    esdk_files_dir = os.path.join(os.path.dirname(os.path.abspath(__file__)), "files")

class OESDKExtTestContextExecutor(OESDKTestContextExecutor):
    _context_class = OESDKExtTestContext

    name = 'esdk'
    help = 'esdk test component'
    description = 'executes esdk tests'

    default_cases = [OESDKTestContextExecutor.default_cases[0],
            os.path.join(os.path.abspath(os.path.dirname(__file__)), 'cases')]
    default_test_data = None

_executor_class = OESDKExtTestContextExecutor
