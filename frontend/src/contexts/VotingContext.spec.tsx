import { render, screen } from '@testing-library/react'
import { VotingProvider, useVoting } from './VotingContext'
import { describe, it, expect } from 'vitest'

describe('VotingContext', () => {
  it('renderiza filhos dentro do VotingProvider', () => {
    render(
      <VotingProvider>
        <div>Teste de filho</div>
      </VotingProvider>
    )
    expect(screen.getByText('Teste de filho')).toBeDefined()
  })

  it('useVoting retorna o valor do contexto', () => {
    let contextValue: unknown
    function TestComponent() {
      contextValue = useVoting()
      return null
    }
    render(
      <VotingProvider>
        <TestComponent />
      </VotingProvider>
    )
    expect(contextValue).toEqual({})
  })

  it('useVoting fora do provider não lança erro e retorna undefined', () => {
    let value
    function TestComponent() {
      value = useVoting()
      return null
    }
    render(<TestComponent />)
    expect(value).toBeUndefined()
  })
})
