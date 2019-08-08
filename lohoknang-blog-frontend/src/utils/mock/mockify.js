import mockList from './mockMap'
import Mock from 'mockjs'

Object.keys(mockList).forEach(key => {
    if (mockList[key].enable) {
        let reg = new RegExp(key)
        Mock.mock(reg, mockList[key].data)
    }
})